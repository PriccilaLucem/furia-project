import React, { useState } from 'react';
import { RegisterContainer } from './styles';
import { Title, InfoText } from '../../styles';
import Form from '../../../components/Form';
import Input from '../../../components/Input';
import Button from '../../../components/Button';
import { toast } from 'react-toastify';
import api from '../../../util/axios';
import { useNavigate } from 'react-router';
import LoginResponse from '../login/interface';

const AdminRegister: React.FC = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const navigate = useNavigate();
  const handleNavigate = () => {
    navigate('/login');
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      toast.error('As senhas não coincidem');
      return;
    }
    api.post<LoginResponse>("/admin/create", formData, {
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        console.log('Registro bem-sucedido:', response.data);
        toast.success('Registro realizado com sucesso!');
        setFormData({ email: '', password: '',  confirmPassword: '' });
        navigate('/admin-login');
      }
      )
      .catch((error) => {
        console.error('Erro no registro:', error);
        toast.error('Erro ao registrar. Verifique seus dados.');
      }
      );
  };

  return (
    <RegisterContainer>
      <Title>Crie seu usuario Admin</Title>
      <Form onSubmit={handleSubmit}>
        <Input 
          type="email" 
          name="email" 
          placeholder="Seu email" 
          value={formData.email} 
          onChange={handleChange} 
          required 
        />
        <Input 
          type="password" 
          name="password" 
          placeholder="Sua senha" 
          value={formData.password} 
          onChange={handleChange} 
          required 
        />
        <Input
          type="password"
          name="confirmPassword"
          placeholder="Confirme sua senha"
          value={formData.confirmPassword}
          onChange={handleChange}
          required
        />
        <Button type="submit">Registrar</Button>
        
      </Form>
      <InfoText onClick={handleNavigate}>Já possui uma conta? Faça login</InfoText>
    </RegisterContainer>
  );
};

export default AdminRegister;

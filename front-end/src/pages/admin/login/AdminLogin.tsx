import React, { useState } from 'react';
import Input from '../../../components/Input';
import Form from '../../../components/Form';
import { LoginContainer } from './styles';
import { Title, InfoText } from '../../styles';
import Button from '../../../components/Button';
import api from '../../../util/axios';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router';
import LoginResponse from './interface';

const AdminLogin: React.FC = () => {
  const [formData, setFormData] = useState({ email: '', password: '' });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    setFormData({ ...formData, [e.target.name]: e.target.value });

  const navigate = useNavigate();
  
  const handleNavigate = () => {
    navigate('/admin-register');
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    api.post<LoginResponse>('/admin/login', formData, {
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(response => {
        console.log('Login bem-sucedido:', response.data);
        toast.success('Login realizado com sucesso!');
        setFormData({ email: '', password: '' });
        localStorage.setItem('token', response.data.token);
        
        navigate("/admin-page")
      })
      .catch(error => {
        console.error('Erro no login:', error);
        toast.error('Erro ao realizar login. Verifique suas credenciais.');
      });
  };

  return (
    <LoginContainer>
      <Title>Admin Login</Title>
      <Form onSubmit={handleSubmit}>
        <Input type="email" name="email" placeholder="Email" value={formData.email} onChange={handleChange} />
        <Input type="password" name="password" placeholder="Senha" value={formData.password} onChange={handleChange} />
        <Button type="submit">Entrar</Button>
      </Form>
      <InfoText onClick={handleNavigate}>Não tem conta? Cadastre-se</InfoText>
    </LoginContainer>
  );
};

export default AdminLogin;

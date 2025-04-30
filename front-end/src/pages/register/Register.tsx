import React, { useState } from 'react';
import { RegisterContainer } from './styles';
import { Title, InfoText } from '../styles';
import Form from '../../components/Form';
import Input from '../../components/Input';
import Button from '../../components/Button';
const Register: React.FC = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Dados de registro:', formData);
    alert('Registro realizado com sucesso!');
    setFormData({ name: '', email: '', password: '' });
  };

  return (
    <RegisterContainer>
      <Title>Crie sua conta</Title>
      <Form onSubmit={handleSubmit}>
        <Input 
          type="text" 
          name="name" 
          placeholder="Seu nome" 
          value={formData.name} 
          onChange={handleChange} 
          required 
        />
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
        <Button type="submit">Registrar</Button>
      </Form>
      <InfoText>Já possui uma conta? <a href="/login">Faça login</a></InfoText>
    </RegisterContainer>
  );
};

export default Register;

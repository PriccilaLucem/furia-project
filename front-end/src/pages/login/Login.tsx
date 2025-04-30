import React, { useState } from 'react';
import Input from '../../components/Input';
import Form from '../../components/Form';
import { LoginContainer } from './styles';
import { Title, InfoText } from '../styles';
import Button from '../../components/Button';

const Login: React.FC = () => {
  const [formData, setFormData] = useState({ email: '', password: '' });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) =>
    setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    alert('Login enviado');
  };

  return (
    <LoginContainer>
      <Title>Entrar na Fúria</Title>
      <Form onSubmit={handleSubmit}>
        <Input type="email" name="email" placeholder="Email" value={formData.email} onChange={handleChange} />
        <Input type="password" name="password" placeholder="Senha" value={formData.password} onChange={handleChange} />
        <Button type="submit">Entrar</Button>
      </Form>
      <InfoText>Não tem conta? <a href="/register">Cadastre-se</a></InfoText>
    </LoginContainer>
  );
};

export default Login;

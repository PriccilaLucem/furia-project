import React, { useState } from 'react';
import { ContactContainer, Title, Paragraph, Form, Input, TextArea, Button, BackButton } from './styles';
import { useNavigate } from 'react-router';
const Contact: React.FC = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    message: '',
  });
  const navigate = useNavigate();
  const handleBUttonClick = () => {
    navigate('/');
};
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Dados enviados:', formData);
    alert('Mensagem enviada com sucesso!');
    setFormData({ name: '', email: '', message: '' });
  };

  return (
    <ContactContainer>
      <Title>Entre em Contato</Title>
      <Paragraph>
        Caso tenha dúvidas, sugestões ou queira saber mais sobre o Projeto Fúria, preencha o formulário abaixo:
      </Paragraph>
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
        <TextArea 
          name="message" 
          placeholder="Sua mensagem" 
          rows={5} 
          value={formData.message} 
          onChange={handleChange} 
          required 
        />
        <Button type="submit">Enviar</Button>
      </Form>
        <BackButton onClick={() => handleBUttonClick()}> Voltar</BackButton>
    </ContactContainer>
  );
};

export default Contact;

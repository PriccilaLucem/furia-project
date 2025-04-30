import React from 'react';
import { Title, Description, Section, BackButton } from './styles';
import { useNavigate } from 'react-router';
import { Container } from '../styles';

const About: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Container>
      <Section>
        <Title>Sobre o Projeto Fúria</Title>
        <Description>
        O Projeto Fúria é uma plataforma dedicada à coleta e gestão de dados de pessoas, 
        com foco em segurança, desempenho e usabilidade.        </Description>
        <Description>
        Seu principal objetivo é fornecer ao administrador uma visão clara e organizada 
        das informações coletadas, permitindo análises eficientes e tomadas de decisão estratégicas.
        </Description>
        <BackButton onClick={() => navigate('/')}>← Voltar</BackButton>
      </Section>
    </Container>
  );
};

export default About;

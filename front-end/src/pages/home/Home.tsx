import React from 'react';
import { useNavigate } from 'react-router';
import {
  CTAButton,
  Description,
  FeatureCard,
  Features,
  Footer,
  Header,
  Logo,
  Main,
  Nav,
  NavButton,
  Title,
} from './styles';
import { Container } from '../styles';

interface navigateProps {
  to: "register" | "login" | "about" | "contact";
}

const Home: React.FC = () => {
    const navigate = useNavigate();
    const handleButtonClick = ({to}:navigateProps) => {
        navigate(to);
    };
  return (
    <Container>
      <Header>
        <Logo>Fúria</Logo>
        <Nav>
            <NavButton onClick={() => handleButtonClick({to:"about"})}>Sobre</NavButton>
            <NavButton onClick={() => handleButtonClick({to:"contact"})}>Contato</NavButton>
        </Nav>
      </Header>

      <Main>
        <Title>Bem-vindo ao Projeto Fúria</Title>
        <Description>
          Uma plataforma moderna, veloz e poderosa. Projetada para desempenho extremo.
        </Description>

        <Features id="features">
          <FeatureCard>
            <h3>🔥 Desempenho</h3>
            <p>Aplicações otimizadas para alta performance e escalabilidade.</p>
          </FeatureCard>

          <FeatureCard>
            <h3>🛡️ Segurança</h3>
            <p>Arquitetura segura com autenticação robusta e proteção de dados.</p>
          </FeatureCard>

          <FeatureCard>
            <h3>⚙️ Flexível</h3>
            <p>Integrações simples com API spring-boot</p>
          </FeatureCard>
        </Features>

        <CTAButton onClick={() => handleButtonClick({to:"register"})}>Comece agora</CTAButton>

        <Footer>
          © {new Date().getFullYear()} Projeto Fúria. Todos os direitos reservados.
        </Footer>
      </Main>
    </Container>
  );
};

export default Home;

import styled from "styled-components";
import { keyframes } from "styled-components";



export const Header = styled.header`
  padding: 20px;
  background-color: #000;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const Logo = styled.h1`
  font-size: 1.8rem;
  color: #e11d48;
`;

export const Nav = styled.nav`
  a {
    margin-left: 20px;
    color: #ccc;
    text-decoration: none;
    &:hover {
      color: #e11d48;
    }
  }
`;

export const Main = styled.main`
  padding: 60px 20px;
  text-align: center;
`;

export const Title = styled.h2`
  font-size: 3rem;
  margin-bottom: 20px;
`;

export const Description = styled.p`
  font-size: 1.2rem;
  color: #bbb;
`;

export const Features = styled.section`
  margin-top: 50px;
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 20px;

  @media (max-width: 768px) {
    flex-direction: column;
    align-items: center;
  }
`;
const hoverUp = keyframes`
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-5px);
  }
`;
export const FeatureCard = styled.div`
  background-color: #1f1f1f;
  padding: 30px;
  border-radius: 10px;
  width: 250px;
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-5px);
    background-color: #2c2c2c;
  }

  h3 {
    color: #e11d48;
    margin-bottom: 10px;
  }

  p {
    color: #aaa;
  }
  transition: transform 0.3s ease;
  &:hover {
    animation: ${hoverUp} 0.3s forwards;
  }
`;

export const Footer = styled.footer`
  margin-top: auto;
  padding: 20px;
  text-align: center;
  background-color: #000;
  color: #555;
`;

export const CTAButton = styled.a`
  margin-top: 40px;
  display: inline-block;
  background-color: #e11d48;
  color: white;
  padding: 12px 24px;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
  transition: background-color 0.3s ease;
  margin: 30px;
  &:hover {
    background-color: #be123c;
  }
`;
export const NavButton = styled.button`
  background: transparent;
  border: none;
  color: #f25;
  font-size: 1rem;
  margin-left: 1.5rem;
  cursor: pointer;
  transition: color 0.3s ease;

  &:hover {
    color: #fff;
  }

  &:focus {
    outline: none;
  }
`;
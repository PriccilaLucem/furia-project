import styled from 'styled-components';

export const Section = styled.section`
  max-width: 800px;
  margin: 4rem auto;
  padding: 2rem;
  text-align: center;
`;

export const Title = styled.h1`
  font-size: 2.5rem;
  color: #f25;
  margin-bottom: 1.5rem;
`;

export const Description = styled.p`
  font-size: 1.1rem;
  color: #ccc;
  margin-bottom: 1.2rem;
  line-height: 1.6;
`;

export const BackButton = styled.button`
  margin-top: 2rem;
  background-color: #f25;
  border: none;
  color: #fff;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s ease;

  &:hover {
    background-color: #c11e1e;
  }
`;

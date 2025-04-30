import styled from 'styled-components';

export const ContactContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 3rem 2rem;
`;

export const Title = styled.h1`
  font-size: 2.5rem;
  margin-bottom: 1.5rem;
  color: #e60000;
`;

export const Paragraph = styled.p`
  font-size: 1.125rem;
  margin-bottom: 2rem;
  color: #444;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Input = styled.input`
  padding: 0.75rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
`;

export const TextArea = styled.textarea`
  padding: 0.75rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
`;

export const Button = styled.button`
  padding: 0.75rem;
  font-size: 1.1rem;
  background-color: #e60000;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;

  &:hover {
    background-color: #cc0000;
  }
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
import React from 'react';
import styled from 'styled-components';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
}

const StyledButton = styled.button`
  background-color: #e60000;
  color: #fff;
  border: none;
  padding: 12px 20px;
  font-size: 1rem;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 100%;

  &:hover {
    background-color: #b30000;
  }

  &:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
`;

const Button: React.FC<ButtonProps> = ({ children, ...rest }) => {
  return <StyledButton {...rest}>{children}</StyledButton>;
};

export default Button;

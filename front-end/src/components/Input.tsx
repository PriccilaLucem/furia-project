import React from 'react';
import styled from 'styled-components';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  type?: string;
  placeholder?: string;
}
const StyledInput = styled.input`
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  outline: none;

  &:focus {
    border-color: #6c63ff;
  }
`;

const Input: React.FC<InputProps> = (props) => {
  return <StyledInput {...props} />;
};

export default Input;

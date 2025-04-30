import React from 'react';
import styled from 'styled-components';

interface FormProps extends React.FormHTMLAttributes<HTMLFormElement> {
  children: React.ReactNode;
}

const StyledForm = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const Form: React.FC<FormProps> = ({ children, ...props }) => {
  return <StyledForm {...props}>{children}</StyledForm>;
};

export default Form;

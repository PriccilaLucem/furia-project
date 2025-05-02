import styled from 'styled-components';

export const FormWrapper = styled.div`
  max-width: 600px;
  margin: 40px auto;
  padding: 2rem;
  background-color: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
`;

export const StyledForm = styled.form`
  display: flex;
  flex-direction: column;
  gap: 2rem; 
`;


export const Fieldset = styled.fieldset`
  border: 1px solid #ddd;
  padding: 1.5rem;
  border-radius: 10px;
  margin-bottom: 2rem;
  background-color: #ffffff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
`;


export const Legend = styled.legend`
  font-weight: 600;
  font-size: 1.1rem;
  padding: 0 0.5rem;
  color: #333;
`;

export const Input = styled.input`
  width: 100%;
  padding: 0.75rem;
  margin-top: 0.25rem;
  margin-bottom: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 1rem;
  background-color: #fff;

  &:focus {
    border-color: #888;
    outline: none;
  }
`;


export const Label = styled.label`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
  font-size: 0.95rem;
  color: #333;
`;


export const Title = styled.h2`
  text-align: center;
  font-size: 2rem;
  margin-bottom: 1.5rem;
  color: #333;
`;

export const SubmitButton = styled.button`
  background-color: #111;
  color: #fff;
  padding: 1rem;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s ease;
  font-size: 1rem;

  &:hover {
    background-color: #222;
  }
`;

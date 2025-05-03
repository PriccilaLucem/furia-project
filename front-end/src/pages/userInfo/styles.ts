import styled from 'styled-components';

// Styled Components with black background and red accents
export const Container = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
  background-color: #000;
  color: #fff;
  min-height: 100vh;
`;

export const Title = styled.h1`
  color: #e60000;
  text-align: center;
  margin-bottom: 30px;
`;

export const InputGroup = styled.div`
  margin-bottom: 15px;
`;

export const Label = styled.label`
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #fff;
`;

export const Input = styled.input`
  width: 100%;
  padding: 8px;
  border: 1px solid #333;
  border-radius: 4px;
  font-size: 16px;
  background-color: #222;
  color: #fff;
  
  &:focus {
    outline: none;
    border-color: #e60000;
  }
`;

export const CheckboxLabel = styled.label`
  display: flex;
  align-items: center;
  gap: 8px;
  color: #fff;
  cursor: pointer;
`;

export const Checkbox = styled.input`
  accent-color: #e60000;
`;

export const Button = styled.button`
  background-color: #e60000;
  color: white;
  padding: 12px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
  width: 100%;
  font-weight: bold;
  transition: background-color 0.2s;

  &:hover {
    background-color: #c50000;
  }
`;

export const UserCard = styled.div`
  border: 1px solid #333;
  border-radius: 4px;
  padding: 15px;
  margin-top: 15px;
  background-color: #111;

  p {
    margin: 5px 0;
  }

  strong {
    color: #e60000;
  }
`;

export const Error = styled.div`
  color: #ff6b6b;
  background-color: #330000;
  padding: 10px;
  border-radius: 4px;
  margin-top: 15px;
`;
export const Loading = styled.p`
  color: #e60000;
  text-align: center;
  margin-top: 20px;
`;
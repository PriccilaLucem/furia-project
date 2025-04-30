import styled from 'styled-components';

export const Container = styled.div`
  background-color: #111;
  color: #fff;
  min-height: 100vh;
  font-family: 'Arial', sans-serif;
`;
export const Title = styled.h1`
  font-size: 2.25rem;
  margin-bottom: 2rem;
  color: #e60000;
  text-align: center;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const InfoText = styled.p`
  margin-top: 1rem;
  font-size: 0.95rem;
  text-align: center;

  a {
    color: #e60000;
    text-decoration: none;
    font-weight: bold;

    &:hover {
      text-decoration: underline;
    }
  }
`;

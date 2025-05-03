import React, { useState } from 'react';
import {
  Container,
  Title,
  InputGroup,
  Label,
  Input,
  Button,
  UserCard,
  Error,
  Loading,
  CheckboxLabel,
  Checkbox,
} from './styles';
import api from '../../util/axios';

export default function UserInfo() {
  const [filters, setFilters] = useState({
    fansScoreMin: '',
    fansScoreMax: '',
    city: '',
    state: '',
    alreadyWentToFuriaEvent: false,
    boughtItems: false,
    eFuriaClubMember: false,
  });

  const [data, setData] = useState<typeof UserInfo[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFilters(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const buildQueryParams = () => {
    const params = new URLSearchParams();
    if (filters.fansScoreMin) params.append('fansScoreMin', filters.fansScoreMin);
    if (filters.fansScoreMax) params.append('fansScoreMax', filters.fansScoreMax);
    if (filters.city) params.append('city', filters.city);
    if (filters.alreadyWentToFuriaEvent) params.append('alreadyWentToFuriaEvent', 'true');
    if (filters.boughtItems) params.append('boughtItems', 'true');
    if (filters.eFuriaClubMember) params.append('eFuriaClubMember', 'true');
    if (filters.state) params.append('state', filters.state);
    return params.toString();
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
  
    if (filters.fansScoreMin && isNaN(Number(filters.fansScoreMin))) {
      setError('O Score mínimo deve ser um número válido');
      setLoading(false);
      return;
    }
  
    if (filters.fansScoreMax && isNaN(Number(filters.fansScoreMax))) {
      setError('O Score máximo deve ser um número válido');
      setLoading(false);
      return;
    }
  
    try {
      const queryString = buildQueryParams();
      const token = localStorage.getItem('token');
      if (!token) {
        setError('Token não encontrado. Faça login novamente.');
        setLoading(false);
        return;
      }
      const response = await api.get(`/user/get-all?${queryString}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log('Response:', response.data);
      setData(response.data as typeof UserInfo[]);
    } catch (err: unknown) {
      // Tratando o erro corretamente
      if (err instanceof Error) {
          setError(err.message || 'Erro desconhecido');
    } else {
        setError('Erro desconhecido');
      }
    } finally {
      setLoading(false);
    }
  };
  

  return (
    <Container>
      <Title>Filtrar Usuários</Title>
      
      <form onSubmit={handleSubmit}>
        <InputGroup>
          <Label>Score mínimo</Label>
          <Input
            type="number"
            name="fansScoreMin"
            value={filters.fansScoreMin}
            onChange={handleChange}
          />
        </InputGroup>

        <InputGroup>
          <Label>Score máximo</Label>
          <Input
            type="number"
            name="fansScoreMax"
            value={filters.fansScoreMax}
            onChange={handleChange}
          />
        </InputGroup>
        <InputGroup>
          <Label>Estado</Label>
            <Input
              type="text"
              name="state"
              value={filters.state}
              onChange={handleChange}
            />
        </InputGroup>

        <InputGroup>
          <Label>Cidade</Label>
          <Input
            type="text"
            name="city"
            value={filters.city}
            onChange={handleChange}
          />
        </InputGroup>

        <InputGroup>
          <CheckboxLabel>
            <Checkbox
              type="checkbox"
              name="alreadyWentToFuriaEvent"
              checked={filters.alreadyWentToFuriaEvent}
              onChange={handleChange}
            />
            Já foi a um evento da Fúria?
          </CheckboxLabel>
        </InputGroup>

        <InputGroup>
          <CheckboxLabel>
            <Checkbox
              type="checkbox"
              name="boughtItems"
              checked={filters.boughtItems}
              onChange={handleChange}
            />
            Já comprou itens?
          </CheckboxLabel>
        </InputGroup>

        <InputGroup>
          <CheckboxLabel>
            <Checkbox
              type="checkbox"
              name="eFuriaClubMember"
              checked={filters.eFuriaClubMember}
              onChange={handleChange}
            />
            Membro do eFuria Club?
          </CheckboxLabel>
        </InputGroup>

        <Button type="submit">Buscar</Button>
      </form>

      {loading && <Loading>Carregando...</Loading>}
      {error && <Error>Erro: {error}</Error>}

  {data && Array.isArray(data) && data.map((user: any) => (
  <UserCard key={user.id}>
    <p><strong>Nome:</strong> {user.name}</p>
    <p><strong>Estado:</strong> {user.address?.state}</p>
    <p><strong>Email:</strong> {user.email}</p>
    <p><strong>Score:</strong> {user.fansScore ?? 'N/A'}</p>
    <p><strong>Cidade:</strong> {user.address?.city ?? 'N/A'}</p>
    <p><strong>Foi a evento:</strong> {user.interaction?.alreadyWentToFuriaEvent ? 'Sim' : 'Não'}</p>
    <p><strong>Comprou itens:</strong> {user.interaction?.boughtItems ? 'Sim' : 'Não'}</p>
    <p><strong>eFuria Club:</strong> {user.interaction?.eFuriaClubMember ? 'Sim' : 'Não'}</p>
  </UserCard>
))}

    </Container>
  );
}

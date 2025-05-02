import { useState } from 'react';
import {
  FormWrapper,
  StyledForm,
  Fieldset,
  Legend,
  Input,
  Label,
  Title,
  SubmitButton,
} from './styles';
import api from '../../util/axios';
import { toast } from 'react-toastify';
import { useForm, Controller } from 'react-hook-form';
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';
import { FormData } from './type';

const defaultValues: FormData = {
  socialMedia: {
    twitterHandle: '',
    instagramHandle: '',
    tiktokHandle: '',
    twitchUsername: '',
    steamUsername: '',
    riotUsername: '',
    followingFuria: false,
  },
  addresses: [
    {
      city: '',
      state: '',
      zip: '',
      country: '',
    },
  ],
  interaction: {
    boughtItems: false,
    efuriaClubMember: false,
    alreadyWentToFuriaEvent: false,
  },
};

const validationSchema = yup.object().shape({
  socialMedia: yup.object().shape({
    twitterHandle: yup.string().required('Twitter handle is required'),
    instagramHandle: yup.string().required('Instagram handle is required'),
    tiktokHandle: yup.string().required('TikTok handle is required'),
    twitchUsername: yup.string().required('Twitch username is required'),
    steamUsername: yup.string().required('Steam username is required'),
    riotUsername: yup.string().required('Riot username is required'),
    followingFuria: yup.boolean().default(false),
  }),
  addresses: yup.array().of(
    yup.object().shape({
      city: yup.string().required('City is required'),
      state: yup.string().required('State is required'),
      zip: yup.string().required('ZIP code is required'),
      country: yup.string().required('Country is required'),
    }).required('Address is required'),
  ).default([
    {
      city: '',
      state: '',
      zip: '',
      country: '',
      user: '',
    },
  ]),
  interaction: yup.object().shape({
    boughtItems: yup.boolean().default(false),
    efuriaClubMember: yup.boolean().default(false),
    alreadyWentToFuriaEvent: yup.boolean().default(false)
  }),
});

export default function FullRegistrationPage() {
  const { control, handleSubmit, formState: { errors } } = useForm<FormData>({
    defaultValues,
    resolver: yupResolver(validationSchema),
  });
  const [formData] = useState({
    socialMedia: {
      twitterHandle: '',
      instagramHandle: '',
      tiktokHandle: '',
      twitchUsername: '',
      steamUsername: '',
      riotUsername: '',
      followingFuria: false,
      userInfo: '',
    },
    addresses: [
      {
        city: '',
        state: '',
        zip: '',
        country: '',
        user: '',
      },
    ],
    interaction: {
      alreadyWentToFuriaEvent: false,
      boughtItems: false,
      efuriaClubMember: false,
    },
  });


  const handleInteractionSubmit = async (token: string) => {
    try {
      await api.post('/interaction/create', formData.interaction, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      toast.success('Interação cadastrada com sucesso!');
    } catch {
      console.error('Erro ao cadastrar interação');
      toast.error('Erro ao cadastrar interação');
    }
  };

  const handleSocialMediaSubmit = async (token: string) => {
    try {
      await api.post('/social-media/create', formData.socialMedia, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      toast.success('Redes sociais cadastradas com sucesso!');
    } catch {
      console.error('Erro ao cadastrar redes sociais');
      toast.error('Erro ao cadastrar redes sociais');
    }
  };
  const handleAddressSubmit = async (token: string) => {
    try {
      await api.post('/address', formData.addresses[0], {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      toast.success('Endereço cadastrado com sucesso!');
    } catch  {
      toast.error('Erro ao cadastrar endereço');
    }
  };

  const onSubmit = async () => {
    const token = localStorage.getItem('token') || '';
    if (!token) {
      toast.error('Token não encontrado. Faça login novamente.');
      return;
    }
    await handleSocialMediaSubmit(token);
    await handleAddressSubmit(token);
    await handleInteractionSubmit(token);
    toast.success('Cadastro completo enviado com sucesso!');
    console.log('Cadastro completo enviado');
  };

  return (
    <FormWrapper>
      <Title>Cadastro Unificado</Title>
      <StyledForm onSubmit={handleSubmit(onSubmit)}>
        <Fieldset>
          <Legend>Redes Sociais</Legend>
          <Controller
            name="socialMedia.twitterHandle"
            control={control}
            render={({ field }) => (
              <Input {...field} placeholder="Twitter" />
            )}
          />
          {errors.socialMedia?.twitterHandle && <span>{errors.socialMedia.twitterHandle.message}</span>}

          <Controller
            name="socialMedia.instagramHandle"
            control={control}
            render={({ field }) => (
              <Input {...field} placeholder="Instagram" />
            )}
          />
          {errors.socialMedia?.instagramHandle && <span>{errors.socialMedia.instagramHandle.message}</span>}

          <Controller
            name="socialMedia.tiktokHandle"
            control={control}
            render={({ field }) => (
              <Input {...field} placeholder="TikTok" />
            )}
          />
          {errors.socialMedia?.tiktokHandle && <span>{errors.socialMedia.tiktokHandle.message}</span>}

          <Controller
            name="socialMedia.twitchUsername"
            control={control}
            render={({ field }) => (
              <Input {...field} placeholder="Twitch" />
            )}
          />
          {errors.socialMedia?.twitchUsername && <span>{errors.socialMedia.twitchUsername.message}</span>}

          <Controller
            name="socialMedia.steamUsername"
            control={control}
            render={({ field }) => (
              <Input {...field} placeholder="Steam" />
            )}
          />
          {errors.socialMedia?.steamUsername && <span>{errors.socialMedia.steamUsername.message}</span>}

          <Controller
            name="socialMedia.riotUsername"
            control={control}
            render={({ field }) => (
              <Input {...field} placeholder="Riot" />
            )}
          />
          {errors.socialMedia?.riotUsername && <span>{errors.socialMedia.riotUsername.message}</span>}

            <Label>
            <Controller
              name="socialMedia.followingFuria"
              control={control}
              render={({ field }) => (
              <input
                {...field}
                type="checkbox"
                checked={field.value || false}
                value={undefined}
                onChange={(e) => field.onChange(e.target.checked)}
              />
              )}
            />
            Segue a FURIA?
            </Label>

        </Fieldset>

        <Fieldset>
          <Legend>Endereço</Legend>
          <Controller
            name="addresses.0.city"
            control={control}
            render={({ field }) => (
              <Input {...field} placeholder="Cidade" />
            )}
          />
          {errors.addresses?.[0]?.city && <span>{errors.addresses[0].city.message}</span>}

          <Controller
            name="addresses.0.state"
            control={control}
            render={({ field }) => (
              <Input {...field} placeholder="Estado" />
            )}
          />
          {errors.addresses?.[0]?.state && <span>{errors.addresses[0].state.message}</span>}

          <Controller
            name="addresses.0.zip"
            control={control}
            render={({ field }) => (
              <Input {...field} placeholder="CEP" />
            )}
          />
          {errors.addresses?.[0]?.zip && <span>{errors.addresses[0].zip.message}</span>}

          <Controller
            name="addresses.0.country"
            control={control}
            render={({ field }) => (
              <Input {...field} placeholder="País" />
            )}
          />
          {errors.addresses?.[0]?.country && <span>{errors.addresses[0].country.message}</span>}

        </Fieldset>

        <Fieldset>
          <Legend>Outros</Legend>
          <Label>
            <Controller
              name="interaction.boughtItems"
              control={control}
              render={({ field }) => (
                <input
                {...field}
                type="checkbox"
                checked={field.value || false}
                onChange={(e) => field.onChange(e.target.checked)}
                value={undefined} 
              />
              )}
            />
            Comprou Itens?
          </Label>

          <Label>
            <Controller
              name="interaction.efuriaClubMember"
              control={control}
              render={({ field }) => (
                <input
                {...field}
                type="checkbox"
                checked={field.value || false}
                onChange={(e) => field.onChange(e.target.checked)}
                value={undefined} 
              />
              )}
            />
            Membro do eFuria Club?
          </Label>
          <Label>
            <Controller
              name="interaction.alreadyWentToFuriaEvent"
              control={control}
              render={({ field }) => (
                <input
                  {...field}
                  type="checkbox"
                  checked={field.value || false}
                  onChange={(e) => field.onChange(e.target.checked)}
                  value={undefined}
                />
              )}
            />
            Já foi a um evento da FURIA?
          </Label>

        </Fieldset>

        <SubmitButton type="submit">Enviar Cadastro</SubmitButton>
      </StyledForm>
    </FormWrapper>
  );
}

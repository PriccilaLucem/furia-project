export interface Filters {
  fansScoreMin?: number;
  fansScoreMax?: number;
  city?: string;
  alreadyWentToFuriaEvent?: boolean;
  boughtItems?: boolean;
  eFuriaClubMember?: boolean;
}

export interface UserInfo {
  id: string;
  name: string;
  email: string;
  fansScore: number;
  address?: {
    city?: string;
  };
  interaction: {
    alreadyWentToFuriaEvent: boolean;
    boughtItems: boolean;
    eFuriaClubMember: boolean;
  };
}
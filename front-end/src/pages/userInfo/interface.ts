export interface User {
  id: string;
  name: string;
  email: string;
  fansScore?: number;
  address?: {
    state?: string;
    city?: string;
  };
  interaction?: {
    alreadyWentToFuriaEvent?: boolean;
    boughtItems?: boolean;
    eFuriaClubMember?: boolean;
  };
}

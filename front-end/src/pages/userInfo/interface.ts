export interface UserInfo {
    id: string;
    name: string;
    fansScore?: number;
    address?: {
      city?: string;
    };
    interaction?: {
      alreadyWentToFuriaEvent?: boolean;
      boughtItems?: boolean;
      eFuriaClubMember?: boolean;
    };
  }
  
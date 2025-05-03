export interface UserInfo {
    id: string;
    name: string;
    fansScore?: number;
    address?: {
      city?: string;
      state?: string;
    };
    interaction?: {
      alreadyWentToFuriaEvent?: boolean;
      boughtItems?: boolean;
      eFuriaClubMember?: boolean;
    };
  }
  
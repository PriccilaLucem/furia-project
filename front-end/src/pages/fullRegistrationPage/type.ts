export type FormData = {
  socialMedia: {
    twitterHandle: string;
    instagramHandle: string;
    tiktokHandle: string;
    twitchUsername: string;
    steamUsername: string;
    riotUsername: string;
    followingFuria: boolean;
  };
  addresses: {
    city: string;
    state: string;
    zip: string;
    country: string;
  }[];
  interaction: {
    boughtItems: boolean;
    efuriaClubMember: boolean;
    alreadyWentToFuriaEvent: boolean;

  };
}
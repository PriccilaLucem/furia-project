import { gql } from '@apollo/client';

export const GET_USER_INFO = gql`
  query GetUserInfo(
    $fansScoreMin: Int
    $fansScoreMax: Int
    $city: String
    $alreadyWentToFuriaEvent: Boolean
    $boughtItems: Boolean
    $eFuriaClubMember: Boolean
  ) {
    userInfo(
      fansScoreMin: $fansScoreMin
      fansScoreMax: $fansScoreMax
      city: $city
      alreadyWentToFuriaEvent: $alreadyWentToFuriaEvent
      boughtItems: $boughtItems
      eFuriaClubMember: $eFuriaClubMember
    ) {
      id
      name
      email
      fansScore
      address {
        city
      }
      interaction {
        alreadyWentToFuriaEvent
        boughtItems
        eFuriaClubMember
      }
    }
  }
`;
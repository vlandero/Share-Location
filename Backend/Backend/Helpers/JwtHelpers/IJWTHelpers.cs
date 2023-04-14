using Backend.Models.DTOs.UserToBeStoredDTO;

namespace Backend.Helpers.JwtHelpers
{
    public interface IJWTHelpers
    {
        string GenerateJwtToken(UserToBeStoredDTO u);
        Guid ValidateJwtToken(string jwtToken);
    }
}

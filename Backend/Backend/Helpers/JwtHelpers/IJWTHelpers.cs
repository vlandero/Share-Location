using Backend.Models.User;

namespace Backend.Helpers.JwtHelpers
{
    public interface IJWTHelpers
    {
        string GenerateJwtToken(User u);
        Guid ValidateJwtToken(string jwtToken);
    }
}

using Backend.Helpers.JwtHelpers;
using Backend.Models.DTOs.UserToBeStoredDTO;
using Backend.Services.UserService;

namespace Tema.Helpers
{
    public class JwtMiddleware
    {
        private readonly RequestDelegate _nextRequestDelegate;

        public JwtMiddleware(RequestDelegate requestDelegate)
        {
            _nextRequestDelegate = requestDelegate;
        }

        public async Task Invoke(HttpContext httpContext, IUserService usersService, IJWTHelpers jwtUtils)
        {
            var token = httpContext.Request.Headers["Authorization"].FirstOrDefault()?.Split("").Last();

            var userId = jwtUtils.ValidateJwtToken(token);

            if (userId != Guid.Empty)
            {
                var usr = usersService.GetById(userId);
                if (usr != null)
                {
                    httpContext.Items["User"] = new UserToBeStoredDTO(usr);
                }
            }

            await _nextRequestDelegate(httpContext);
        }
    }
}
using Backend.Models.DTOs.ManyToManyDTO;
using Backend.Models.DTOs.UserRegisterRequestDTO;
using Backend.Models.DTOs.UserToBeStoredDTO;
using Backend.Services.UserService;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private readonly IUserService _userService;
        public UsersController(IUserService userService)
        {
            _userService = userService;
        }
        [HttpPost("register")]
        public IActionResult Register(UserRegisterRequestDTO u)
        {
            try
            { 
                var res = _userService.Registration(u);
                return Ok(res);
            }
            catch (Microsoft.EntityFrameworkCore.DbUpdateException ex)
            {
                return BadRequest(ex.InnerException.Message);
            }
        }
        [HttpPost("modify")]
        public IActionResult Modify(UserToBeStoredDTO user)
        {
            try
            {
                var modifiedUser = _userService.ModifyUser(user);
                return Ok(modifiedUser);
            }
            catch(Exception ex)
            {
                if(ex.InnerException == null)
                {
                    return BadRequest(ex.Message);
                }
                return BadRequest(ex.InnerException.Message);

            }
        }
        [HttpPost("connect")]
        public IActionResult Connect(ManyToManyDTO con)
        {
            try
            {
                _userService.Connect(con.Id1, con.Id2);
                return Ok(con);
            }
            catch(Exception ex)
            {
                if (ex.InnerException == null)
                {
                    return BadRequest(ex.Message);
                }
                return BadRequest(ex.InnerException.Message);
            }
        }
        [HttpPost("reject")]
        public IActionResult Reject(ManyToManyDTO rej)
        {
            try
            {
                _userService.Reject(rej.Id1, rej.Id2);
                return Ok(rej);
            }
            catch (Exception ex)
            {
                if (ex.InnerException == null)
                {
                    return BadRequest(ex.Message);
                }
                return BadRequest(ex.InnerException.Message);
            }
        }
        [HttpGet("feed/{username}")]
        public IActionResult GetFeed(string username)
        {
            try
            {
                var user = _userService.GetByUsername(username);
                var users = _userService.GetUsersForFeed(user.Id);
                return Ok(users);
            }
            catch (Exception ex)
            {
                if (ex.InnerException == null)
                {
                    return BadRequest(ex.Message);
                }
                return BadRequest(ex.InnerException.Message);
            }


        }

    }
}

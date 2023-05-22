using Backend.Models.DTOs.ManyToManyDTO;
using Backend.Models.DTOs.UserLoginRequestDTO;
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
        private IActionResult _getException(Exception e)
        {
            if (e.InnerException == null)
            {
                return BadRequest(e.Message);
            }
            return BadRequest(e.InnerException.Message);
        }
        [HttpPost("register")]
        public IActionResult Register(UserRegisterRequestDTO u)
        {
            try
            { 
                var res = _userService.Registration(u);
                return Ok(res);
            }
            catch (Exception ex)
            {
                return _getException(ex);
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
                return _getException(ex);
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
                return _getException(ex);
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
                return _getException(ex);
            }
        }
        [HttpGet("feed/{username}")]
        public IActionResult GetFeed(string username)
        {
            try
            {
                var user = _userService.GetByUsername(username);
                if(user == null)
                {
                    return BadRequest("User not found");
                }
                var users = _userService.GetUsersForFeed(user.Id);
                return Ok(users);
            }
            catch (Exception ex)
            {
                return _getException(ex);
            }
        }
        [HttpDelete("delete-connection")]
        public IActionResult DeleteConnection(ManyToManyDTO con)
        {
            try
            {
                _userService.DeleteConnection(con.Id1, con.Id2);
                return Ok(con);
            }
            catch (Exception ex)
            {
                return _getException(ex);
            }
        }
        [HttpDelete("delete-rejection")]
        public IActionResult DeleteRejection(ManyToManyDTO rej)
        {
            try
            {
                _userService.DeleteRejection(rej.Id1, rej.Id2);
                return Ok(rej);
            }
            catch (Exception ex)
            {
                return _getException(ex);
            }
        }
        [HttpPost("login")]
        public IActionResult Login(UserLoginRequestDTO user)
        {
            try
            {
                var res = _userService.Login(user);
                return Ok(res);
            }
            catch (Exception ex)
            {
                return _getException(ex);
            }
        }
        [HttpGet("get-by-username/{username}")]
        public IActionResult GetByUsername(string username)
        {
            try
            {
                var user = _userService.GetByUsername(username);
                if(user == null)
                {
                    return BadRequest("User not found");
                }
                return Ok(user);
            }
            catch (Exception ex)
            {
                return _getException(ex);
            }
        }

        //delete all users
        [HttpDelete("delete-all")]
        public IActionResult DeleteAll()
        {
            try
            {
                _userService.DeleteAll();
                return Ok();
            }
            catch (Exception ex)
            {
                return _getException(ex);
            }
        }


    }
}

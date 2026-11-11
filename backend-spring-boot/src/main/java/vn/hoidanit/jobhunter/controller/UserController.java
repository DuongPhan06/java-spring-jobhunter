package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.cj.jdbc.result.UpdatableResultSet;
import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.DTO.ResUserDTO;
import vn.hoidanit.jobhunter.domain.DTO.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.DTO.UpdateUserResponseDTO;
import vn.hoidanit.jobhunter.domain.DTO.UserResponseDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiMessage("Create a user successfully")
    @PostMapping("/user")
    public ResponseEntity<UserResponseDTO> getCreateUser(@RequestBody User user) throws IdInvalidException {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        // kiểm tra có trùng email
        if (this.userService.CheckExistsEmail(user.getEmail())) {
            throw new IdInvalidException("Email " + user.getEmail() + "đã tồn tại, vui lòng thử email khác");
        }

        // Lưu người dùng
        User user1 = this.userService.handleCreateUser(user);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user1.getId());
        userResponseDTO.setName(user1.getName());
        userResponseDTO.setGender(user1.getGender());
        userResponseDTO.setEmail(user1.getEmail());
        userResponseDTO.setAddress(user1.getAddress());
        userResponseDTO.setAge(user1.getAge());
        userResponseDTO.setCreatedAt(user1.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> getDeleteUser(@PathVariable long id) throws IdInvalidException {
        if (id > 1500) {
            throw new IdInvalidException("Id khong lon hon 1500");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete User Sucessfully");
    }

    @GetMapping("/user/{id}")
    @ApiMessage("Fetch User By Id")
    public ResponseEntity<ResUserDTO> FetchUserbyId(@PathVariable long id) throws IdInvalidException {
        User user = this.userService.fetchUserById(id);
        if (user == null) {
            throw new IdInvalidException("User trên không tồn tại, vui lòng lấy User khác !" );
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(user));
    }

    @GetMapping("/user")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> FetchAllUser(@Filter Specification<User> spec, Pageable pageable) {

        // String s_Current = currentOptional.isPresent() ? currentOptional.get() : "";
        // String s_pagesize = pageSizeOptional.isPresent() ? pageSizeOptional.get() :
        // "";
        // int currentPage = Integer.parseInt(s_Current);
        // int currentSize = Integer.parseInt(s_pagesize);
        // Pageable pageable = PageRequest.of(currentPage - 1, currentSize);

        ResultPaginationDTO resultPaginationDTO = this.userService.fetchAllUsers(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }

    @PutMapping("/user")
    public ResponseEntity<UpdateUserResponseDTO> getUpdateUser(@RequestBody User user) throws IdInvalidException {
        User new_user = this.userService.handleUpdateUser(user);
        if (new_user == null) {
            throw new IdInvalidException("Id " + user.getId() + " Không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.GetConvert(new_user));
    }
}

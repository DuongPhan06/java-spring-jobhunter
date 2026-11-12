package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.DTO.Meta;
import vn.hoidanit.jobhunter.domain.DTO.ResUserDTO;
import vn.hoidanit.jobhunter.domain.DTO.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.DTO.UpdateUserResponseDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(Long id) {
        if (id != null) {
            this.userRepository.deleteById(id);
        }
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> usersPage = this.userRepository.findAll(spec, pageable);
        Meta meta = new Meta();
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setTotalPages(usersPage.getTotalPages());
        meta.setTotalElements(usersPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        // resultPaginationDTO.setResult(usersPage.getContent());
        List<ResUserDTO> listUser = usersPage.getContent()
                .stream()
                .map(item -> new ResUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()))
                .collect(Collectors.toList());

        resultPaginationDTO.setResult(listUser);
        return resultPaginationDTO;
    }

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setGender(reqUser.getGender());
            currentUser.setAge(reqUser.getAge());
            currentUser.setName(reqUser.getName());
            // update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User getUserbyUserName(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean CheckExistsEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public boolean CheckExistsID(long id) {
        return this.userRepository.existsById(id);
    }

    public UpdateUserResponseDTO GetConvert(User updatedUser) {
        UpdateUserResponseDTO updateUserResponseDTO = new UpdateUserResponseDTO();
        updateUserResponseDTO.setAge(updatedUser.getAge());
        updateUserResponseDTO.setId(updatedUser.getId());
        updateUserResponseDTO.setName(updatedUser.getName());
        updateUserResponseDTO.setGender(updatedUser.getGender());
        updateUserResponseDTO.setUpdatedAt(updatedUser.getUpdatedAt());
        updateUserResponseDTO.setAddress(updatedUser.getAddress());
        return updateUserResponseDTO;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

}

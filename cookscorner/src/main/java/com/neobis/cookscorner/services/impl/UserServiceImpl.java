package com.neobis.cookscorner.services.impl;

import com.neobis.cookscorner.dtos.recipe.RecipeResponseDto;
import com.neobis.cookscorner.dtos.user.UserEditDto;
import com.neobis.cookscorner.dtos.user.UserResponseDto;
import com.neobis.cookscorner.entities.Recipe;
import com.neobis.cookscorner.entities.User;
import com.neobis.cookscorner.entities.UserSaves;
import com.neobis.cookscorner.exceptions.ApiCommonException;
import com.neobis.cookscorner.repositories.RecipeRepository;
import com.neobis.cookscorner.repositories.UserRepository;
import com.neobis.cookscorner.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RecipeRepository recipeRepository;

    @Override
    public Optional<User> findUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public UserResponseDto findUserForProfile(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        userResponseDto.setRecipes(createRecipeResponseDtoListFromRecipes(user.getRecipes()));
        userResponseDto.setSaves(createRecipeResponseDtoListFromUserSaves(user.getSaves()));
        userResponseDto.setLikesCount(user.getLikes().size());
        userResponseDto.setFollowersCount(user.getFollowers().size());
        userResponseDto.setFollowingsCount(user.getFollowings().size());
        return userResponseDto;
    }

    @Override
    public UserResponseDto findUserForProfile(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiCommonException("User not found"));
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        userResponseDto.setRecipes(createRecipeResponseDtoListFromRecipes(user.getRecipes()));
        userResponseDto.setSaves(createRecipeResponseDtoListFromUserSaves(user.getSaves()));
        userResponseDto.setLikesCount(user.getLikes().size());
        userResponseDto.setFollowersCount(user.getFollowers().size());
        userResponseDto.setFollowingsCount(user.getFollowings().size());
        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> findAllByNameStartsWith(String name) {
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        List<User> users = userRepository.findAllByNameStartsWithIgnoreCase(name);
        fillUserResponseDtos(userResponseDtos, users);
        return userResponseDtos;
    }

    private void fillUserResponseDtos(List<UserResponseDto> userResponseDtos, List<User> users) {
        for(User user: users) {
            UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
            userResponseDto.setRecipes(createRecipeResponseDtoListFromRecipes(user.getRecipes()));
            userResponseDto.setSaves(createRecipeResponseDtoListFromUserSaves(user.getSaves()));
            userResponseDto.setLikesCount(user.getLikes().size());
            userResponseDto.setFollowersCount(user.getFollowers().size());
            userResponseDto.setFollowingsCount(user.getFollowings().size());
            userResponseDto.setSaves(null);
            userResponseDtos.add(userResponseDto);
        }
    }

    @Transactional
    @Override
    public void editUser(UserEditDto userEditDto, String email) {
        if (userEditDto == null)
            throw new ApiCommonException("User name and description should not be null");
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
        user.setName(userEditDto.getName());
        user.setDescription(userEditDto.getDescription());
        String photoUrl;
        // TODO: Save photo
        if(userEditDto.getPhoto() != null) {
            try {
                if(!userEditDto.getPhoto().getContentType().equals("image/png"))
                    throw new ApiCommonException("Photo should be in png format");
//                link = new StringBuilder("http://localhost:8080/api/users/photos/");
//                String photoName = userEditDto.getPhoto().getOriginalFilename() + UUID.randomUUID().toString();
//                link.append(photoName);
//                UserPhoto userPhoto = UserPhoto
//                        .builder()
//                        .name(photoName)
//                        .type(userEditDto.getPhoto().getContentType())
//                        .imageData(userEditDto.getPhoto().getBytes())
//                        .build();
//                userPhotoRepository.save(userPhoto);
                photoUrl = savePhoto(userEditDto.getPhoto());
                user.setPhotoLink(photoUrl);
            } catch (IOException e) {
                throw new ApiCommonException("Error in photo saving");
            }
            user.setPhotoLink(photoUrl);
        }
        user.setEmail(email);
        userRepository.save(user);
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        // Генерация пути к файлу и сохранение
        Path uploadPath = Paths.get("src/main/resources/photos");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Возвращаем путь к файлу
        return "http://165.227.147.154:8081/api/photos/" + filename;
    }



    @Transactional
    @Override
    public void followUser(Long id, String email) {
        User userToFollow = userRepository.findById(id).orElseThrow(() -> new ApiCommonException(String.format("User with id %d not found", id)));
        User userCurrent = userRepository.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
        if(userCurrent.getId().equals(userToFollow.getId()))
            throw new ApiCommonException("User can't follow or unfollow himself");
        userToFollow.getFollowers().add(userCurrent);
        userCurrent.getFollowings().add(userToFollow);
    }

    @Transactional
    @Override
    public void unFollowUser(Long id, String email) {
        User userToFollow = userRepository.findById(id).orElseThrow(() -> new ApiCommonException(String.format("User with id %d not found", id)));
        User userCurrent = userRepository.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
        if(userCurrent.getId().equals(userToFollow.getId()))
            throw new ApiCommonException("User can't follow or unfollow himself");
        userToFollow.getFollowers().remove(userCurrent);
        userCurrent.getFollowings().remove(userToFollow);
    }



//    @Override
//    public byte[] findPhotoByName(String photoName) {
//        return userPhotoRepository.findUserPhotoByName(photoName).orElseThrow(() -> new ApiCommonException("Image not found")).getImageData();
//    }

//    @Override
//    public void editUser(MultipartFile photo, String email) {
//        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
//        // TODO: Save photo
//        if(photo != null) {
//            user.setPhoto(photo.getContentType());
//        }
//        userRepository.save(user);
//    }
//
//    @Override
//    public void editUser(UserEditDto userEditDto, String email) {
//        if (userEditDto == null)
//            throw new ApiCommonException("User name and description should not be null");
//        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new ApiCommonException("User not found"));
//        user.setName(userEditDto.getName());
//        user.setDescription(userEditDto.getDescription());
//        userRepository.save(user);
//    }

    private List<RecipeResponseDto> createRecipeResponseDtoListFromRecipes(List<Recipe> recipes) {
        List<RecipeResponseDto> recipeResponseDtos = new ArrayList<>();
        if (recipes != null) {
            for(Recipe recipe: recipes) {
                RecipeResponseDto recipeResponseDto = modelMapper.map(recipe, RecipeResponseDto.class);
                recipeResponseDto.setLikesCount(recipe.getLikes().size());
                recipeResponseDto.setSavesCount(recipe.getSaves().size());
                recipeResponseDtos.add(recipeResponseDto);
            }
        }

        return recipeResponseDtos;
    }

    private List<RecipeResponseDto> createRecipeResponseDtoListFromUserSaves(List<UserSaves> saves) {
        List<RecipeResponseDto> recipeResponseDtos = new ArrayList<>();
        if (saves != null) {
            for(UserSaves userSave: saves) {
                Recipe recipe = userSave.getRecipe();
                RecipeResponseDto recipeResponseDto = modelMapper.map(recipe, RecipeResponseDto.class);
                recipeResponseDto.setLikesCount(recipe.getLikes().size());
                recipeResponseDto.setSavesCount(recipe.getSaves().size());
                recipeResponseDtos.add(recipeResponseDto);
            }
        }

        return recipeResponseDtos;
    }

}

package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.user.UserManagementDto;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.FriendshipRepo;
import greencity.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepo friendshipRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<UserManagementDto> findFriendsByUserId(Long id) {
        validateUserExistence(id);

        List<Long> friendIds = friendshipRepo.findFriendIdsByUserId(id);

        return userRepo.findAllById(friendIds)
                .stream()
                .map(friend -> modelMapper.map(friend, UserManagementDto.class))
                .collect(Collectors.toList());
    }

    private void validateUserExistence(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + userId);
        }
    }
}

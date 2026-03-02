package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.dto.user.UserManagementDto;
import greencity.dto.user.UserVO;
import greencity.service.FriendshipService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/friends")
public class FriendshipController {
    private final FriendshipService friendshipService;

    @GetMapping("/my")
    public ResponseEntity<List<UserManagementDto>> getMyFriends(@ApiIgnore @CurrentUser UserVO user) {
        return ResponseEntity.status(HttpStatus.OK).body(friendshipService.findFriendsByUserId(user.getId()));
    }
}

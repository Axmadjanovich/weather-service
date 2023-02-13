package uz.gc.weatherservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.dto.SubscriptionDto;

public interface SubscriptionService {
    /**
     * Subscribe to selected city. User info is taken from ReactiveSecurityContextHolder
     * @param cityId ID of selected city.
     * @return ResponseDto with SubscriptionDto which represents subscription time and City
     */
    Mono<ResponseDto<SubscriptionDto>> subscribe(Integer cityId);

    /**
     * Get all subscriptions of logged-in user.
     * @return All subscribed cities and subscription date of user
     */
    Flux<SubscriptionDto> getAllSubscriptions();

    /**
     * This method is for admins. Getting all subscriptions of any user by userId
     * @param userId ID of user
     * @return Flux of SubscriptionDto
     */
    Flux<SubscriptionDto> getUserSubscriptions(Integer userId);
}

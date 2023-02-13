package uz.gc.weatherservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.gc.weatherservice.dto.ResponseDto;
import uz.gc.weatherservice.dto.SubscriptionDto;
import uz.gc.weatherservice.service.SubscriptionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("subscribe")
public class SubscriptionResource {
    private final SubscriptionService subsctiptionService;

    @PostMapping("subscribe-to-city")
    public Mono<ResponseDto<SubscriptionDto>> subscribeToCity(@RequestParam Integer cityId){
        return subsctiptionService.subscribe(cityId);
    }

    @GetMapping("my-subscriptions")
    public Flux<SubscriptionDto> getSubscriptions(){
        return subsctiptionService.getAllSubscriptions();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("user-details")
    public Flux<SubscriptionDto> getUserSubscriptions(@RequestParam Integer userId){
        return subsctiptionService.getUserSubscriptions(userId);
    }
}

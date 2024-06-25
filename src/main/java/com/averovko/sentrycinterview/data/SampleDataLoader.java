package com.averovko.sentrycinterview.data;

import com.averovko.sentrycinterview.database.entity.*;
import com.averovko.sentrycinterview.database.repository.MarketplaceRepository;
import com.averovko.sentrycinterview.database.repository.ProducersRepository;
import com.averovko.sentrycinterview.database.repository.SellerInfoRepository;
import com.averovko.sentrycinterview.database.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.*;

@RequiredArgsConstructor
//@Component
public class SampleDataLoader implements ApplicationRunner {
    private final ProducersRepository producersRepository;
    private final MarketplaceRepository marketplaceRepository;
    private final SellerInfoRepository sellerInfoRepository;
    private final SellerRepository sellerRepository;

    private static final Random RANDOM = new Random();

    private static List<String> producerNames = Arrays.asList(
            "Adidas", "Admiral", "ASICS", "Brooks", "Burrda Sport",
            "Castore", "Champion", "Charly", "Columbia", "Cosco",
            "Diadora", "Duneus", "Ellesse", "Erreà", "ERKE",
            "FBT", "Fila", "Grand Sport", "Hummel", "Jako",
            "Joma", "Kappa", "Kelme", "Lacoste", "Le Coq Sportif",
            "Legea", "Lonsdale", "Lotto", "Macron", "Majid, Inc.",
            "Marathon", "Mitre", "Mizuno", "New Balance", "Nike",
            "Nivia", "Olympikus", "Patrick", "Peak", "Penalty",
            "Pirma", "Provogue", "Puma", "Quick", "Reebok",
            "Reusch", "Romai", "Saeta", "Select", "Seven",
            "SG", "SIX5SIX", "SS", "Topper", "TYKA",
            "Uhlsport", "Umbro", "Under Armour", "Wilson", "Xtep");

    private static List<String> marketplaceNames = Arrays.asList(
            "Amazon", "Decathlon", "Alltricks", "Bol", "Zalando",
            "Beslist", "Conrad", "Colizey", "VidaXL", "Veepee",
            "Traininn", "Go-SPORT", "eBay", "Lids", "Wehkamp",
            "Coolblue", "Plutosport", "REI", "SportPursuit", "Backcountry",
            "BikeExchange", "Deporvillage", "Wiggle", "Snowleader", "Moosejaw");

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var producers = producerNames
                .stream()
                .map(name -> {
                    var producer = new Producer();
                    producer.setName(name);
                    return producer;
                })
                .toList();
        var savedProducers = producersRepository.saveAllAndFlush(producers);

        var marketplaces = marketplaceNames
                .stream()
                .map(name -> {
                    var marketplace = new Marketplace();
                    marketplace.setDescription(name + " description");
                    return marketplace;
                })
                .toList();
        var savedMarketplaces = marketplaceRepository.saveAllAndFlush(marketplaces);

        var sellerInfos = savedMarketplaces
                .stream()
                .map(marketplace -> {
                    var name = marketplace.getDescription().split(" ")[0];
                    var sellerInfo = new SellerInfo();
                    sellerInfo.setMarketplace(marketplace);
                    sellerInfo.setName(name);
                    sellerInfo.setUrl(name.toLowerCase() + ".com");
                    sellerInfo.setCountry("Germany");
                    sellerInfo.setExternalId(UUID.randomUUID().toString());
                    return sellerInfo;
                })
                .toList();
        var savedSellerInfos = sellerInfoRepository.saveAllAndFlush(sellerInfos);

        List<Seller> sellers = new ArrayList<>();
        for (Producer producer : savedProducers){
            for (SellerInfo sellerInfo : savedSellerInfos){
                var seller = new Seller();
                seller.setProducer(producer);
                seller.setSellerInfo(sellerInfo);
                seller.setState(SellerState.values()[RANDOM.nextInt(SellerState.values().length)]);
                sellers.add(seller);
            }
        }
        sellerRepository.saveAllAndFlush(sellers);
    }
}

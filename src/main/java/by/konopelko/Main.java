package by.konopelko;

import by.konopelko.model.Animal;
import by.konopelko.model.Car;
import by.konopelko.model.Flower;
import by.konopelko.model.House;
import by.konopelko.model.Person;
import by.konopelko.util.Util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyPair;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        task1();
        task2();
        task3();
        task4();
        task5();
        task6();
        task7();
        task8();
        task9();
        task10();
        task11();
        task12();
        task13();
        task14();
        task15();
        task16();
    }

    private static void task1() throws IOException {
        List<Animal> animals = Util.getAnimals();
        final AtomicInteger counter = new AtomicInteger();
        animals.stream()
                .filter(animal -> animal.getAge() >= 10 && animal.getAge() <= 20)
                .sorted(Comparator.comparing(Animal::getAge))
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 7))
                .get(2)
                .forEach(System.out::println);
    }

    private static void task2() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> "Japanese".equals(animal.getOrigin()))
                .peek(animal -> animal.setBread(animal.getBread().toUpperCase()))
                .filter(animal -> "Female".equals(animal.getGender()))
                .map(Animal::getBread)
                .forEach(System.out::println);
    }

    private static void task3() throws IOException {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> animal.getAge() > 30 && animal.getOrigin().startsWith("A"))
                .map(Animal::getOrigin)
                .distinct()
                .forEach(System.out::println);
//
    }

    private static void task4() throws IOException {
        List<Animal> animals = Util.getAnimals();
        long count = animals.stream()
                .filter(animal -> "Female".equals(animal.getGender()))
                .count();
        System.out.println(count);
    }

    private static void task5() throws IOException {
        List<Animal> animals = Util.getAnimals();
        boolean isHungarian = animals.stream()
                .anyMatch(animal -> animal.getAge() >= 20 && animal.getAge() <= 30
                        && "Hungarian".equals(animal.getOrigin()));
        System.out.println(isHungarian);
    }

    private static void task6() throws IOException {
        List<Animal> animals = Util.getAnimals();
        boolean isGender = animals.stream()
                .allMatch(animal -> "Male".equals(animal.getGender())
                        || "Female".equals(animal.getGender()));
        System.out.println(isGender);
    }

    private static void task7() throws IOException {
        List<Animal> animals = Util.getAnimals();
        boolean isFromOceania = animals.stream()
                .noneMatch(animal -> "Oceania".equals(animal.getOrigin()));
        System.out.println(isFromOceania);
    }

    private static void task8() throws IOException {
        List<Animal> animals = Util.getAnimals();
        Animal oldestAnimal = animals.stream()
                .sorted(Comparator.comparing(Animal::getBread))
                .limit(100)
                .max(Comparator.comparing(Animal::getAge))
                .get();
        System.out.println(oldestAnimal.getAge());
    }

    private static void task9() throws IOException {
        List<Animal> animals = Util.getAnimals();
        int minLengthOfBread = animals.stream()
                .mapToInt(animal -> animal.getBread().toCharArray().length)
                .min()
                .getAsInt();
        System.out.println(minLengthOfBread);
    }

    private static void task10() throws IOException {
        List<Animal> animals = Util.getAnimals();
        int sumAge = animals.stream()
                .mapToInt(Animal::getAge)
                .sum();
        System.out.println(sumAge);
    }

    private static void task11() throws IOException {
        List<Animal> animals = Util.getAnimals();
        double averageAge = animals.stream()
                .filter(animal -> "Indonesian".equals(animal.getOrigin()))
                .mapToInt(Animal::getAge)
                .average()
                .getAsDouble();
        System.out.println(averageAge);
    }

    private static void task12() throws IOException {
        List<Person> people = Util.getPersons();
        people.stream()
                .filter(person -> "Male".equals(person.getGender())
                        && Period.between(person.getDateOfBirth(), LocalDate.now()).getYears() >= 18
                        && Period.between(person.getDateOfBirth(), LocalDate.now()).getYears() <= 27)
                .sorted(Comparator.comparing(Person::getRecruitmentGroup))
                .limit(200)
                .forEach(System.out::println);

    }

    private static void task13() throws IOException {
        List<House> houses = Util.getHouses();
        List<Person> firstWaveOfPersons = houses.stream()
                .filter(house -> "Hospital".equals(house.getBuildingType()))
                .flatMap(house -> house.getPersonList().stream())
                .collect(Collectors.toList());
        Map<Boolean, List<Person>> personsFromCivilBuildingByAge = houses.stream()
                .filter(house -> !"Hospital".equals(house.getBuildingType()))
                .flatMap(house -> house.getPersonList().stream())
                .collect(Collectors.partitioningBy(person ->
                        Period.between(person.getDateOfBirth(), LocalDate.now()).getYears() < 18
                                || Period.between(person.getDateOfBirth(), LocalDate.now()).getYears() > 65));
        firstWaveOfPersons.addAll(personsFromCivilBuildingByAge.get(true));
        firstWaveOfPersons.addAll(personsFromCivilBuildingByAge.get(false));
        firstWaveOfPersons.stream()
                .limit(500)
                .forEach(System.out::println);
    }

    private static void task14() throws IOException {
        List<Car> cars = Util.getCars();
        final Map<String, List<Car>> carsByCountry = new HashMap<>();

        Map<Boolean, List<Car>> carsForOne = cars.stream()
                .collect(Collectors.partitioningBy(car -> "Jaguar".equals(car.getCarMake())
                        || "White".equals(car.getColor())));
        carsByCountry.put("Туркменистан", carsForOne.get(true));

        Map<Boolean, List<Car>> carsForTwo = carsForOne.get(false).stream()
                .collect(Collectors.partitioningBy(car -> car.getMass() < 1500
                        && List.of("BMW", "Lexus", "Chrysler", "Toyota").contains(car.getCarMake())));
        carsByCountry.put("Узбекистан", carsForTwo.get(true));

        Map<Boolean, List<Car>> carsForThree = carsForTwo.get(false).stream()
                .collect(Collectors.partitioningBy(car -> (car.getMass() > 4000 && "Black".equals(car.getColor()))
                        || List.of("GMC", "Dodge").contains(car.getCarMake())));
        carsByCountry.put("Казахстан", carsForThree.get(true));

        Map<Boolean, List<Car>> carsForFour = carsForThree.get(false).stream()
                .collect(Collectors.partitioningBy(car -> car.getReleaseYear() < 1982
                        || List.of("Civic", "Cherokee").contains(car.getCarModel())));
        carsByCountry.put("Кыргызстан", carsForFour.get(true));

        Map<Boolean, List<Car>> carsForFive = carsForFour.get(false).stream()
                .collect(Collectors.partitioningBy(car -> car.getPrice() > 40000
                        || !List.of("Yellow", "Red", "Green", "Blue").contains(car.getColor())));
        carsByCountry.put("Россия", carsForFive.get(true));

        Map<Boolean, List<Car>> carsForSix = carsForFive.get(false).stream()
                .collect(Collectors.partitioningBy(car -> car.getVin().contains("59")));
        carsByCountry.put("Монголия", carsForSix.get(true));

        carsByCountry.forEach((country, carList) -> {
            double cost = carList.stream()
                    .mapToDouble(car -> car.getMass() * 7.14)
                    .sum();
            System.out.println(country + ", транспортные расходы:" +
                    BigDecimal.valueOf(cost).setScale(2, RoundingMode.HALF_UP));
        });
        double totalAmount = carsByCountry.values().stream()
                .flatMap(Collection::stream)
                .mapToDouble(car -> car.getMass() * 7.14)
                .sum();
        System.out.println("Выручка логистической компании: "
                + BigDecimal.valueOf(totalAmount).setScale(2, RoundingMode.HALF_UP));

    }

    private static void task15() throws IOException {
        List<Flower> flowers = Util.getFlowers();
        double totalSum = flowers.stream()
                .sorted(Comparator.comparing(Flower::getOrigin)
                        .reversed()
                        .thenComparing(Flower::getPrice)
                        .thenComparing(Flower::getWaterConsumptionPerDay)
                        .reversed())
                .filter(flower -> flower.getCommonName().matches("^[C-S].*")
                        && flower.isShadePreferred()
                        && (flower.getFlowerVaseMaterial().contains("Steel")
                        || flower.getFlowerVaseMaterial().contains("Aluminum")
                        || flower.getFlowerVaseMaterial().contains("Glass")))
                .mapToDouble(flower -> flower.getPrice() + flower.getWaterConsumptionPerDay() * 365 * 5 * 1.35)
                .sum();
        System.out.println(BigDecimal.valueOf(totalSum).setScale(2, RoundingMode.HALF_UP));
    }

    private static void task16() throws IOException {

    }
}
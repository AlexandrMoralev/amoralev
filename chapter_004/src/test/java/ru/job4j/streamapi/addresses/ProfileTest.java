package ru.job4j.streamapi.addresses;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * ProfileTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class ProfileTest {

    private static final Address SPB = new Address("SPb", "Nevsky", 2, 2);
    private static final Address DC = new Address("DC", "Central", 1, 1);
    private static final Address DC2 = new Address("DC", "Prospect", 1, 1);
    private static final Address NN = new Address("NN", "Gorky st", 100, 500);
    private static final Address EKB = new Address("Ekb", "Chelyabinskaya", 101, 500);
    private static final Address KIZHI = new Address("KIZHI", "Lenina", 1, 1);
    private static final Address ARZAMAS = new Address("Arzamas", "Lenina", 1, 1);
    private static final Address ARZAMAS16 = new Address("Arzamas", "Secret st", 0, 0);

    private final List<Address> over10M = List.of(DC, SPB, DC2);
    private final List<Address> over1M = List.of(NN, EKB);
    private final List<Address> less100K = List.of(KIZHI, ARZAMAS16, ARZAMAS);

    private final List<Address> allAddresses = Stream.of(over10M, over1M, less100K).flatMap(Collection::stream).collect(Collectors.toList());


    @Test
    public void collectAddressesTest() {
        assertTrue(
                Profile.collectAddresses(
                        List.of(new Profile(DC), new Profile(SPB), new Profile(DC2))
                ).containsAll(over10M)
        );
        assertThat(Profile.collectAddresses(
                List.of(new Profile(DC), new Profile(SPB))),
                not(over1M)
        );
        assertTrue(
                Profile.collectAddresses(
                        List.of(new Profile(NN), new Profile(EKB))
                ).containsAll(over1M)
        );
        assertThat(Profile.collectAddresses(
                List.of(new Profile(NN), new Profile(EKB))),
                not(less100K)
        );

        assertTrue(
                Profile.collectAddresses(
                        List.of(new Profile(KIZHI), new Profile(ARZAMAS), new Profile(ARZAMAS16))
                ).containsAll(less100K)
        );
        assertThat(Profile.collectAddresses(
                List.of(new Profile(KIZHI), new Profile(ARZAMAS))),
                not(over10M)
        );
    }

    @Test
    public void collectEmptyAddressesTest() {
        assertThat(Profile.collectAddresses(new ArrayList<>()), is(Collections.<Address>emptyList()));
    }

    @Test
    public void collectAllAddressesTest() {
        assertTrue(
                Profile.collectAddresses(
                        Stream.of(DC2, KIZHI, ARZAMAS, NN, EKB, DC, SPB, ARZAMAS16)
                                .map(Profile::new)
                                .collect(Collectors.toList()))
                        .containsAll(allAddresses));
    }
}

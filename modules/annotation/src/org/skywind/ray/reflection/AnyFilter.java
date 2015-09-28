package org.skywind.ray.reflection;

import org.skywind.ray.meta.InterfaceAudience;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 22:09
 */
@InterfaceAudience.Private
public class AnyFilter {

    public AnyFilter(String[] locations) {
        for (String location : locations) {
            includePackage(location);
        }
    }

    public boolean check(String value) {
        for (Predicate predicate : predicates) {
            if (predicate.evaluate(value)) {
                return true;
            }
        }
        return false;
    }

    private interface Predicate {

        boolean evaluate(String t);
    }

    private List<Predicate> predicates = new ArrayList<>();

    private AnyFilter includePackage(String prefix) {
        return add(new Include(prefix(prefix)));
    }

    private AnyFilter add(Predicate filter) {
        predicates.add(filter);
        return this;
    }

    private static String prefix(String qualifiedName) {
        return qualifiedName.replace(".", "\\.") + ".*";
    }

    private static class Include implements Predicate {
        final Pattern pattern;

        public Include(final String patternString) {
            pattern = Pattern.compile(patternString);
        }

        @Override
        public boolean evaluate(final String regex) {
            return pattern.matcher(regex).matches();
        }
    }
}

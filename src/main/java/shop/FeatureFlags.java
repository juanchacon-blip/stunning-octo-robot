package shop;

import java.util.HashSet;
import java.util.Set;

public class FeatureFlags {
    private final Set<String> enabled = new HashSet<>();
    public void enable(String name)  { enabled.add(name); }
    public void disable(String name) { enabled.remove(name); }
    public boolean isEnabled(String name) { return enabled.contains(name); }
}

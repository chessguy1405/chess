@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    <ClassName> that = (<ClassName>) obj;
    return <value>.equals(that.<value>);
}

@Override
public int hashCode() {
    return 31 * Objects.hashCode(<value>);
}

@Override
public String toString() {
    return String.format("<example string>"); // %s inserts a string value, %n inserts a newline
}
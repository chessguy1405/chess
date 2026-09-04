public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    <ClassName> that = (<ClassName>) o;
    return <value>.equals(that.<value>);
}

public int hashCode() {
    return 31 * Objects.hashCode(<value>)
}

public String toString() {
    return String.format("<example string>") // %s inserts a string value, %n inserts a newline
}
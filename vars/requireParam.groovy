void call(String paramValue, String paramName) {

    if (!paramValue?.trim()) {
        error "$paramName nicht gesetzt."
    }
}

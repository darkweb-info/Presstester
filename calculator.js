let display = document.getElementById("display");

function press(value) {
    if (display.value === "0" || display.value === "Ошибка") {
        display.value = value;
        return;
    }

    if (value === ".") {
        let lastNumber = display.value.split(/[\+\-\*\/\%\(\)]/).pop();
        if (lastNumber.includes(".")) return;
    }

    display.value += value;
}

function clearDisplay() {
    display.value = "0";
}

function backspace() {
    display.value = display.value.length > 1
        ? display.value.slice(0, -1)
        : "0";
}

function calculate() {
    try {
        let exp = display.value.replace(/(\d+(\.\d+)?)%/g, "($1/100)");
        display.value = eval(exp);
    } catch {
        display.value = "Ошибка";
    }
}

document.addEventListener("keydown", function(event) {
    let key = event.key;

    if (key === "Enter") {
        event.preventDefault();
        calculate();
        return;
    }

    if (/[0-9]/.test(key)) {
        press(key);
    } else if (key === "." || key === "," || event.code === "NumpadDecimal") {
        press(".");
    } else if (["+", "-", "*", "/", "%", "(", ")"].includes(key)) {
        press(key);
    } else if (key === "Backspace") {
        event.preventDefault();
        backspace();
    } else if (key === "Escape") {
        clearDisplay();
    }
});

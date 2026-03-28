document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");

    // ---------------- Create error containers ----------------
    const fields = ["cardHolderName", "cardNumber", "expiryMonth", "expiryYear", "cvv"];
    fields.forEach(field => {
        const input = form[field];
        let errorSpan = document.createElement("div");
        errorSpan.className = "text-danger mt-1 small";
        errorSpan.id = field + "-error";
        input.insertAdjacentElement("afterend", errorSpan);
    });

    // ---------------- Form validation ----------------
    form.addEventListener("submit", function (event) {
        let valid = true;

        // Clear old errors
        fields.forEach(field => {
            document.getElementById(field + "-error").innerText = "";
        });

        // Card Holder Name
        const name = form.cardHolderName.value.trim();
        if (!/^[a-zA-Z ]+$/.test(name)) {
            valid = false;
            document.getElementById("cardHolderName-error").innerText =
                "Card Holder Name must contain only letters and spaces.";
        }

        // Card Number
        const cardNumber = form.cardNumber.value.trim();
        if (!/^\d{16}$/.test(cardNumber)) {
            valid = false;
            document.getElementById("cardNumber-error").innerText =
                "Card Number must be 16 digits.";
        }

        // Expiry Month
        const month = form.expiryMonth.value.trim();
        if (!/^(0[1-9]|1[0-2])$/.test(month)) {
            valid = false;
            document.getElementById("expiryMonth-error").innerText =
                "Expiry Month must be between 01 and 12.";
        }

        // Expiry Year (4 digits)
        const year = form.expiryYear.value.trim();
        const currentYear = new Date().getFullYear(); // 4-digit year
        if (!/^\d{4}$/.test(year) || parseInt(year) < currentYear) {
            valid = false;
            document.getElementById("expiryYear-error").innerText =
                "Expiry Year must be this year or later (YYYY format).";
        }

        // CVV
        const cvv = form.cvv.value.trim();
        if (!/^\d{3}$/.test(cvv)) {
            valid = false;
            document.getElementById("cvv-error").innerText =
                "CVV must be 3 digits.";
        }

        if (!valid) {
            event.preventDefault(); // Stop form submission
        }
    });

    // ---------------- Back-button prevention ----------------
    if (window.history && window.history.replaceState) {
        window.history.replaceState(null, null, window.location.href);
    }

    window.onpageshow = function(event) {
        if (event.persisted || (window.performance && window.performance.navigation.type === 2)) {
            window.location.href = '/';
        }
    };
});

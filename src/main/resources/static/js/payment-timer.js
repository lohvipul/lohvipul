// payment-timer.js
window.addEventListener('DOMContentLoaded', (event) => {
    let totalTime = 2 * 60 + 30; // 2 minutes 30 seconds
    const timerDiv = document.getElementById('timer');
    const payButton = document.querySelector('button[type="submit"]');

    const countdown = setInterval(() => {
        let minutes = Math.floor(totalTime / 60);
        let seconds = totalTime % 60;

        minutes = minutes < 10 ? '0' + minutes : minutes;
        seconds = seconds < 10 ? '0' + seconds : seconds;

        timerDiv.innerText = `Time left: ${minutes}:${seconds}`;

        if (totalTime <= 0) {
            clearInterval(countdown);
            payButton.disabled = true;
            timerDiv.innerText = "Time expired! Payment disabled.";
            alert("Payment time expired.");
        }

        totalTime--;
    }, 1000);
});

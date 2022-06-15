$(document).ready(function() {
    $('.header_burger').click(function (event) {
        //добавляем класс active при клике
        //toggle - чтобы при повтоном клике класс убирался
        $('.header_burger, .header_menu').toggleClass('active');
        // запрещаем скрол при открытом меню
        $('body').toggleClass('lock');
    });
    $('.dots-menu_icon').click(function (event) {
        if (!$(this.closest('.clickable')).hasClass('active') && $('.clickable').hasClass('active')) {
            $('.clickable.active').toggleClass('active');
        }
        $(this.closest('.clickable')).toggleClass('active');
    });
    $('.status_done').appendTo($('.archive'));
    document.querySelectorAll('.countable').forEach(counter);
});

function counter(countable) {
    const input = countable.querySelector('.text-field__input');
    const counter = countable.querySelector('.text-field__counter');
    const input_maxLength = input.maxLength;
    let len = input.value.length;
    counter.textContent = `${len} / ${input_maxLength}`;
    const updateCounter = (e) => {
        len = e ? e.target.value.length : 0;
        counter.textContent = `${len} / ${input_maxLength}`;
    }
    input.addEventListener('click', updateCounter);
    input.addEventListener('keyup', updateCounter);
    input.addEventListener('keydown', updateCounter);
}
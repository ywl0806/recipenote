const addBtn = document.querySelectorAll(".add-ingredient");
const amounts = document.querySelectorAll('.amount');
let resultList = [];
const Ingredient = function (id, name, price) {
    this.id = id;
    this.name = name;
    this.price = price;
}

//amount change
amounts.forEach(item =>{
    item.addEventListener('change',function (){
        const index = item.id.substring(item.id.indexOf('_') + 1 , item.id.length)
        if ($(`#name_${index}`).val() !== null){
            const $total = $(`#total_${index}`);
            const $price = $(`#price_${index}`);

            $total.text((item.value * $price.val()).toFixed(1));
            const $totalPrice = $('#total_price');
            $totalPrice.text(0);
            for (let i = 0; i < 30; i++) {
                if ($(`#price_${i}`).val()!==0){
                    const TPNum = Number($totalPrice.text());
                    const TNum = Number($(`#total_${i}`).text());
                    $totalPrice.text(TPNum + TNum);
                }
            }

        }
    })
})

//event of add ingredient
addBtn.forEach(item => {
    item.addEventListener("click", function () {
        const createIngBtn = document.getElementById('create-ingredient-submit');
        const $alert = $('.ing-upload-alert');
        const inputValue = document.getElementById('input-value');
        const searchValue = document.getElementById('search-value');
        const listContainer = document.getElementById('list-container');
        const $searchBtn = $('#search-btn');
        const name = document.getElementById('ingredient-name');
        const gram = document.getElementById('ingredient-gram');
        const price = document.getElementById('ingredient-price');

        let jsonValue;
        const rootId = item.id;
        const rIndex = rootId.substring(rootId.indexOf('_')+1,rootId.length);
        const resultCon = new Ingredient();
        name.value = "";
        gram.value = "";
        price.value = "";
        searchValue.value = "";
        resultList = [];
        $(document).ready(function () {
            //modal off
            $('#ingredient-modal').on('hidden.bs.modal', function () {
                removeAllChild(listContainer);
                inputValue.textContent = "";
                $searchBtn.off('click')
                $('#search-result-input').off('click');
                resultList = [];
            })
        })
        //input button event
        $('#search-result-input').on('click',function (){
            $(`#name_${rIndex}`).val(resultCon.name);
            $(`#price_${rIndex}`).val(resultCon.price);
            $(`#id_${rIndex}`).val(resultCon.id);

            $('#ingredient-modal').modal('hide')
        })
        //modal on
        $('#ingredient-modal').modal();
        let page = 0;

        //search function
        function search() {
            resultList = [];
            const keyword = searchValue.value
            let index = 0;
            $.ajax('/search-ingredient', {
                method: 'GET',
                data: {
                    'keyword': keyword,
                    'page': page
                },
                success: function (result) {
                    console.log(result);
                    result.forEach(function (item) {
                        const tr = document.createElement('tr');
                        const E_name = document.createElement('td');
                        const E_price = document.createElement('td');
                        const ing = new Ingredient(item.id, item.name, item.price);

                        resultList.push(ing);


                        E_name.value = index;
                        E_price.value = index;
                        index++;
                        tr.classList.add('point');
                        tr.append(E_name);
                        tr.append(E_price);

                        E_name.textContent = item.name;
                        E_price.textContent = item.price;
                        listContainer.append(tr);
                    })
                }

            })

        }

        //search event
        $searchBtn.on('click', function () {
            removeAllChild(listContainer);
            search();
        });
        //searched ingredient click event
        $(document).on('click', '.point', function (event) {
            const i = event.target.value;
            resultCon.name = resultList[i].name;
            resultCon.id = resultList[i].id;
            resultCon.price = resultList[i].price;

            inputValue.textContent = resultCon.name;
        });



        //create ingredient
        createIngBtn.addEventListener("click", function () {

            if (gram.value === "" && price.value === "") {
                console.log("가격과 무게가 널임");
            } else {
                const g_per_price = Math.round((price.value / gram.value + Number.EPSILON) * 100) / 100;
                console.log(g_per_price);
                $alert.removeClass('alert-success alert-warning')

                $.ajax('/create-ingredient', {
                    method: 'POST',
                    data: JSON.stringify({
                        "name": name.value,
                        "price": g_per_price
                    }),
                    contentType: "application/json",

                    success: function (result) {
                        $alert.show().addClass('alert-success').text('Upload success');
                        document.getElementById('collapse').ariaExpanded = "false";
                        document.getElementById('create-ingredient').classList.remove('show');
                        name.value = "";
                        gram.value = "";
                        price.value = "";
                        jsonValue = JSON.parse(result)
                        inputValue.textContent = jsonValue.name
                    },

                    error: function () {
                        $alert.show().addClass('alert-warning').text('Upload error');
                    },

                })
            }

        })
    })
})

//remove all child function
const removeAllChild = (node) => {
    while (node.hasChildNodes()) {
        node.removeChild(node.lastChild);
    }
}
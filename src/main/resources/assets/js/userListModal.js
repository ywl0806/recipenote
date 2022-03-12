const $modalOn = $('#search-modal');
const resultItem = document.getElementById('result-item');
const memberImg = document.getElementById('member_avatar');
const memberEmail = document.getElementById('member_email');
const memberName = document.getElementById('member_name');
const memberId = document.getElementById('member_id');

$modalOn.click(function (){
    const $searchBtn = $('#search-btn');
    const $searchInput = $('#search-input');

    $('#modal').modal('show');
    $searchBtn.click(function (){
        const keyword = $searchInput.val();
        $.ajax('/search-member',{
            method: 'GET',
            data: {
                'email' :keyword
            },
            success: function (result){
                if (Object.keys(result).length===0){
                    alert("검색결과가 없습니다.");
                }else{
                    if (result.avatarUrl !== null){
                        memberImg.src = result.avatarUrl;
                    }
                    $modalOn.val(result.email);
                    memberName.textContent = result.name;
                    memberEmail.textContent = result.email;
                    memberId.value = result.id;
                    $('#modal').modal('hide');
                }
            }
        })
    })

})
$(document).ready(function () {
    //modal off
    $('#modal').on('hidden.bs.modal', function () {
        $('#search-btn').off('click');
        $('#search-input').val('');
    })

})
const removeAllChild = (node) => {
    while (node.hasChildNodes()) {
        node.removeChild(node.lastChild);
    }}

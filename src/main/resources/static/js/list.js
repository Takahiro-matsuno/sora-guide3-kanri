function listClicked(id){
        //フォーム作成
        var form = document.createElement("form");
        form.setAttribute("action", "./delete");
        form.setAttribute("method", "get");
        form.style.display = "none";
        document.body.appendChild(form);

        //送信値
        var input = document.createElement('input');
        input.setAttribute('type',  'hidden');
        input.setAttribute('name', 'id');
        input.setAttribute('value', id);
        form.appendChild(input);

        form.submit();
    }
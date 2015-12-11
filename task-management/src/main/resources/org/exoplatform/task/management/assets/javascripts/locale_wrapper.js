(function($) {
    var locale = {
        dueDate: '${label.dueDate}',
        coworkers: '${label.coworkers}',
        coworker: '${label.coworker}',
        unassigned: '${label.unassigned}',
        tag: '${label.tag}',
        noMatch: '${label.noMatch}',
        remove: '${label.remove}',
        selectUser: '${popup.selectUser}',
        noManager: '${message.noManager}',
        noParticipator: '${message.noParticipator}',
        taskDescriptionEmpty: '${editinline.taskDescription.empty}',
        noPermissionToAccessProject: '${popup.msg.noPermissionToAccessProject}',
        taskPlan: {
            errorMessage: '${editinline.taskPlan.errorMessage}'
        }
    };

    locale.resolve = function(key, params) {
        var message = key;
        if (locale[key] != undefined) {
            message = locale[key];
        }
        if (params != undefined) {
            if (!$.isArray(params)) {
                params = [params];
            }

            for (var i = 0; i < params.length; i++) {
                var reg = new RegExp('\\{' + i + '\\}', 'g');
                message = message.replace(reg, params);
            }
        }
        return message;
    };

    return locale;
})($);
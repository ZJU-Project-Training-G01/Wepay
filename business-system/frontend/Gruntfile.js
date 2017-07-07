module.exports = function(grunt) {
    var orderItemPath = 'components/orderItem';
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        sass: {
            output: {
                files: [{
                    expand: true,
                    cwd: orderItemPath,
                    src: 'sidebar.scss',
                    dest: orderItemPath,
                    ext: '.css'
                }]
            }
        },
        watch: {
            scripts: {
                files: [orderItemPath + '/sidebar.scss'],
                tasks: ['sass']
            },
            livereload: {
                options: {
                    livereload: '<%=connect.options.livereload %>'
                },
                files: [
                    orderItemPath + '/siderbar.css'

                ]
            }
        },
        connect: {
            options: {
                port: 9001,
                open: true,
                livereload: 35729,
                hostname: 'localhost'
            },
            server: {
                options: {
                    port: 9001,
                    base: "./"
                }
            }
        }

    });
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-connect')


    grunt.registerTask('outputcss', ['sass']);
    grunt.registerTask('watchit', ['outputcss', 'connect', 'watch']);

    grunt.registerTask('default');
};
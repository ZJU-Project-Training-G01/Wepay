module.exports = function(grunt) {
    var sidebarPath = 'components/sidebar';
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        sass: {
            output: {
                files: [{
                    expand: true,
                    cwd: sidebarPath,
                    src: 'sidebar.scss',
                    dest: sidebarPath,
                    ext: '.css'
                }]
            }
        },
        watch: {
            scripts: {
                files: [sidebarPath + '/sidebar.scss'],
                tasks: ['sass']
            },
            livereload: {
                options: {
                    livereload: '<%=connect.options.livereload %>'
                },
                files: [
                    sidebarPath + '/siderbar.css'

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
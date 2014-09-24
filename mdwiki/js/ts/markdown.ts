declare var marked: any;

interface JQuery {
    toptext: () => string;
}
class Markdown {
    public markdownSource: string;
    public options: any;

    private defaultOptions: any = {
        gfm: true,
        tables: true,
        breaks: true
    };
    constructor(markdownSource: string, options: any = {}) {
        this.markdownSource = markdownSource;
        this.options = options;
    }

    transform(): string {
        marked.setOptions(this.options);
        var uglyHtml = marked(this.markdownSource);
        return uglyHtml;
    }
}

class Navbar {
    private navbarMarkdown: string;
    private uglyHtml;
    constructor(navbarMarkdown: string) {
        this.navbarMarkdown = navbarMarkdown;
        var md = new Markdown(navbarMarkdown);
        this.uglyHtml = md.transform();
    }
    render() {
        var h = $('<div>' + this.uglyHtml + '</div>');
        // TODO .html() is evil!!!
        h.find('p').each(function(i,e) {
            var el = $(e);
            el.replaceWith(el.html());
        });
        $('#md-menu').append(h.html());
    }
    hideIfHasNoLinks() {
        var num_links = $('#md-menu a').length;
        var has_header = $('#md-menu .navbar-brand').eq(0).toptext().trim().length > 0;
        if (!has_header && num_links <= 1)
            $('#md-menu').hide();
    }
}

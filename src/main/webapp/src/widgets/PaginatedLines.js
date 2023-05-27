import React, {Component} from 'react';
import Button from "./Button";
import LinesGroup from "./LinesGroup";

const PAGE_SIZE = 10;

export default class PaginatedLines extends Component {
    constructor(props) {
        super(props);
        this.state = {
            page: 0
        }
    }

    render() {
        const pageSize = this.props.pageSize || PAGE_SIZE;
        const page = this.state.page;
        let pageButtons = this.buildPageButtons();
        let lines = React.Children.toArray(this.props.children);

        let visibleLines = [];
        for (let i = page; i < page + pageSize; i++) {
            if (i < lines.length) {
                visibleLines.push(lines[i]);
            } else {
                visibleLines.push(<div key={i}/>);
            }
        }

        return <div className="row">
            <div className="col-sm-12">
                <div className="row">
                    <div className="col-sm-12">
                        <LinesGroup vertical={true} justified={true}>
                            {visibleLines}
                        </LinesGroup>
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-12">
                        {pageButtons}
                    </div>
                </div>
            </div>
        </div>;
    }

    selectPage(index) {
        this.setState({
            page: index
        });
    }

    buildDefaultButtons(pages, page) {
        return <div className="btn-group">
            <Button disabled={page <= 0}
                    commitAction={this.selectPage.bind(this, page - 1)}
                    icon="arrow-up"/>
            <Button disabled={page >= pages - 1}
                    commitAction={this.selectPage.bind(this, page + 1)}
                    icon="arrow-down"/>
        </div>
    }

    buildPageButtons() {
        const page = this.state.page;
        const pages = React.Children.toArray(this.props.children).length - (this.props.pageSize || PAGE_SIZE);

        let buttons = this.buildDefaultButtons(pages, page);

        return <nav className="top-sep">
            {buttons}
        </nav>;
    }
}
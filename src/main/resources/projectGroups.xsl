<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>

    <xsl:param name="project"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>
                    <xsl:copy-of select="$project"/>
                    - Groups
                </title>
            </head>
            <body>
                <p>
                    <xsl:copy-of select="$project"/> groups:
                </p>
                <table>
                    <tr>
                        <th>Group name</th>
                    </tr>
                    <xsl:for-each select="/*[name()='Payload']/*[name()='Projects']/*[name()='project']/*[name()='name'][text()=$project]/following-sibling::*[name()='Groups']/*[name()='group']">
                        <tr>
                            <td>
                                <xsl:copy-of select="@name"/>
                            </td>
                        </tr>
                    </xsl:for-each>

                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:strip-space elements="*"/>


</xsl:stylesheet>
using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Backend.Migrations
{
    /// <inheritdoc />
    public partial class ManyToMany : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Connecteds",
                columns: table => new
                {
                    Id1 = table.Column<Guid>(type: "uuid", nullable: false),
                    Id2 = table.Column<Guid>(type: "uuid", nullable: false),
                    User1Id = table.Column<Guid>(type: "uuid", nullable: false),
                    User2Id = table.Column<Guid>(type: "uuid", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Connecteds", x => new { x.Id1, x.Id2 });
                    table.ForeignKey(
                        name: "FK_Connecteds_Users_User1Id",
                        column: x => x.User1Id,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Connecteds_Users_User2Id",
                        column: x => x.User2Id,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Rejecteds",
                columns: table => new
                {
                    Id1 = table.Column<Guid>(type: "uuid", nullable: false),
                    Id2 = table.Column<Guid>(type: "uuid", nullable: false),
                    User1Id = table.Column<Guid>(type: "uuid", nullable: false),
                    User2Id = table.Column<Guid>(type: "uuid", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Rejecteds", x => new { x.Id1, x.Id2 });
                    table.ForeignKey(
                        name: "FK_Rejecteds_Users_User1Id",
                        column: x => x.User1Id,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Rejecteds_Users_User2Id",
                        column: x => x.User2Id,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Connecteds_User1Id",
                table: "Connecteds",
                column: "User1Id");

            migrationBuilder.CreateIndex(
                name: "IX_Connecteds_User2Id",
                table: "Connecteds",
                column: "User2Id");

            migrationBuilder.CreateIndex(
                name: "IX_Rejecteds_User1Id",
                table: "Rejecteds",
                column: "User1Id");

            migrationBuilder.CreateIndex(
                name: "IX_Rejecteds_User2Id",
                table: "Rejecteds",
                column: "User2Id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Connecteds");

            migrationBuilder.DropTable(
                name: "Rejecteds");
        }
    }
}
